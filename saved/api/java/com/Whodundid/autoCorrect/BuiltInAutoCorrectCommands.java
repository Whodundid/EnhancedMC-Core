package com.Whodundid.autoCorrect;

public class BuiltInAutoCorrectCommands {

	protected AutoCorrectApp man;
	public AutoCorrectCommand back, brsphere, build, copy, flist, hcyl, lobby, paste, undo;
	
	public BuiltInAutoCorrectCommands(AutoCorrectApp manIn) {
		man = manIn;
		back = new AutoCorrectCommand("/back");
		brsphere = new AutoCorrectCommand("//brsphere");
		build = new AutoCorrectCommand("/build");
		copy = new AutoCorrectCommand("//copy");
		flist = new AutoCorrectCommand("/f list");
		hcyl = new AutoCorrectCommand("//hcyl");
		lobby = new AutoCorrectCommand("/lobby");
		paste = new AutoCorrectCommand("//paste");
		undo = new AutoCorrectCommand("/undo");
		addDefaultAliases();
	}
	
	private void addDefaultAliases() {
		back.addAlias("bcak");
		back.addAlias("bcka");
		back.addAlias("baco");
		back.addAlias("bakc");
		back.addAlias("ack");
		back.addAlias("bcak");
		
		brsphere.addAlias("/brsphere");
		brsphere.addAlias("/brphsere");
		brsphere.addAlias("/brspher");
		brsphere.addAlias("/brpsher");
		brsphere.addAlias("/brpshere");
		
		build.addAlias("buidl");
		build.addAlias("bidl");
		build.addAlias("buidlk");
		build.addAlias("buidln");
		build.addAlias("buidjl");
		build.addAlias("buiold");
		build.addAlias("buid;l");
		build.addAlias("buid'");
		build.addAlias("buid");
		build.addAlias("ubild");
		build.addAlias("ubildf");
		build.addAlias("bujdk.");
		build.addAlias("budil");
		build.addAlias("bvuild");
		build.addAlias("builpod");
		build.addAlias("biulod");
		build.addAlias("builkd");
		build.addAlias("bu8oid");
		build.addAlias("bujidl");
		build.addAlias(".bujhhdl");
		build.addAlias("bjuiol;d");
		build.addAlias("budli");
		build.addAlias("budioo");
		build.addAlias("builds");
		build.addAlias("biudl");
		build.addAlias("buiidl");
		build.addAlias("buiilds");
		build.addAlias("buidol");
		build.addAlias("build'");
		build.addAlias("ubiold");
		build.addAlias("biuodl;");
		build.addAlias("biuodl");
		build.addAlias("biuod");
		build.addAlias("biuo");
		build.addAlias("buiodl");
		build.addAlias("bjidl");
		build.addAlias("bui9d");
		build.addAlias("buid''");
		build.addAlias("biudo;");
		build.addAlias("biudo");
		build.addAlias("biud");
		build.addAlias("buidls");
		
		copy.addAlias("/cpoy");
		copy.addAlias("/coy");
		copy.addAlias("/vpoy");
		copy.addAlias("/vopy");
		copy.addAlias("/opcy");
		
		hcyl.addAlias("/hycl");
		hcyl.addAlias("/hcly");
		
		lobby.addAlias("lboy");
		lobby.addAlias("lboby");
		lobby.addAlias("lobpyu");
		lobby.addAlias("lboyb");
		lobby.addAlias("l;obyb");
		lobby.addAlias("lobyb");
		lobby.addAlias("lbyb");
		
		paste.addAlias("/pasetd");
		paste.addAlias("/apstee");
		paste.addAlias("/apste");
		paste.addAlias("/pastee");
		paste.addAlias("/past");
		paste.addAlias("/paset");
		
		undo.addAlias("/u");
		undo.addAlias("/unod");
	}
	
	public BuiltInAutoCorrectCommands registerCommands() {
		man.addCommand(back);
		man.addCommand(brsphere);
		man.addCommand(build);
		man.addCommand(copy);
		man.addCommand(flist);
		man.addCommand(hcyl);
		man.addCommand(lobby);
		man.addCommand(paste);
		man.addCommand(undo);
		return this;
	}
}
